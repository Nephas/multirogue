(ns rlserver.generate.level.level
  (:require
    [rllib.board :refer [rect rect-array large-neighborhood midpoint]]
    [rllib.vector :refer [man-dist sub add]]
    [rllib.rand :refer [set-seed! uniform rand-n rand-coll new-seed]]
    [rllib.state :refer [apply-seq apply-times]]
    [rlserver.generate.npc :refer [generate-godi]]
    [rlserver.generate.level.castle :refer [generate-castle-level]]
    [rlserver.generate.level.ruin :refer [generate-ruin-level]]
    [rlserver.generate.level.frost :refer [generate-frost-level]]
    [rlserver.generate.object :refer [generate-goal generate-stair-up generate-stair-down generate-corpse generate-potion]]
    [rlserver.generate.pc :refer [generate-pc]]))

(defn bsp-partition [[[x y] w h]]
  (let [rand-slice #(int (* % (rand-coll [2/3 3/5 2/5 1/3])))]
    (if (> w h) (let [w1 (rand-slice w)
                      w2 (- w w1)]
                  [[[x y] w1 h]
                   [[(+ x w1) y] w2 h]])
                (let [h1 (rand-slice h)
                      h2 (- h h1)]
                  [[[x y] w h1]
                   [[x (+ y h1)] w h2]]))))

(defn bsp-rooms [[x y] n]
  (let [t0 [[[1 1] (- x 2) (- y 2)]]]
    (loop [tier n
           defs [t0]]
      (if (zero? tier) defs
                       (recur (dec tier)
                              (conj defs (->> (last defs)
                                              (map bsp-partition)
                                              (apply concat))))))))

(defn bsp-tree-corridors [room-defs w]
  (loop [remaining-rooms room-defs
         corridors '()]
    (if (empty? remaining-rooms) corridors
                                 (recur (rest (rest remaining-rooms))
                                        (conj corridors [(midpoint (first remaining-rooms))
                                                         (midpoint (second remaining-rooms)) w])))))

(defn bsp-corridors [tier-defs w]
  (apply concat (map #(bsp-tree-corridors % w) (rest tier-defs))))

(defn open-fields [tier-defs corridor-defs]
  (let [corridor-fields (map (fn [[p1 p2 w]] (rect p1 (add p2 [w w]))) corridor-defs)
        room-fields (map (fn [[[x y] w h]] (rect [x y] w h 2)) (last tier-defs))
        rect-fields (concat corridor-fields room-fields)]
    (distinct (apply concat rect-fields))))

(defn wallmap [size tier-defs corridor-defs]
  (-> (rect-array size 0)
      (apply-seq (open-fields tier-defs corridor-defs)
                 (fn [m p] (assoc-in m p 1)))))

(defn get-biome [lvlid]
  (get {1 :castle
        2 :ruin
        3 :ruin
        4 :frost
        5 :frost} lvlid))

(defn get-size [lvlid]
  (get {1 [64 64]
        2 [56 56]
        3 [48 48]
        4 [40 40]
        5 [32 32]} lvlid))

(defn get-tiers [lvlid]
  (get {1 4
        2 3
        3 3
        4 3
        5 2} lvlid))

(defn get-corridorsize [lvlid]
  (get {1 1
        2 1
        3 2
        4 2
        5 3} lvlid))

(defn get-generator [biome]
  (get {:castle generate-castle-level
        :ruin   generate-ruin-level
        :frost  generate-frost-level} biome))

(defn generate-exit [state pos lvl]
  (if (= lvl 5)
    (generate-goal state pos lvl)
    (generate-stair-down state pos [lvl (inc lvl)])))

(defn generate-level [state lvl fromlvl]
  (set-seed! lvl)
  (let [size (get-size lvl)
        biome (get-biome lvl)

        tier-defs (bsp-rooms size (get-tiers lvl))
        corridor-defs (bsp-corridors tier-defs (get-corridorsize lvl))
        open (wallmap size tier-defs corridor-defs)

        entry-pos (midpoint (rand-coll (last tier-defs)))
        exit-pos (apply max-key (fn [pos] (man-dist pos entry-pos))
                          (map midpoint (last tier-defs)))]
    (-> state
        (merge {:level   lvl
                :mapsize size
                :open    open
                :maphash (hash open)
                :biome   biome})

        (generate-stair-up entry-pos [lvl (if (not= 1 lvl) (dec lvl))])
        (generate-exit exit-pos lvl)
        ((get-generator biome) tier-defs corridor-defs))))

(defn with-new-pcs [state]
  (let [[transitid _] (first (filter (fn [[_ [fromlvl tolvl]]] (= tolvl nil)) (:transition state)))
        fields (vals (large-neighborhood (get-in state [:pos transitid])))]
    (-> state
        (generate-pc 0 "Hervor" (rand-coll fields))
        (generate-pc 1 "Yrsa" (rand-coll fields))
        (generate-godi (rand-coll fields)))))
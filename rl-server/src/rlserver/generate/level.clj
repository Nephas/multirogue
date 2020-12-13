(ns rlserver.generate.level
  (:require
    [rllib.board :refer [rect]]
    [rllib.rand :refer [set-seed! uniform rand-n rand-coll new-seed]]
    [rlserver.entity.state :refer [apply-seq apply-times]]
    [rlserver.generate.npc :refer [generate-bat generate-skeleton]]
    [rlserver.generate.object :refer [generate-stair-up generate-stair-down generate-corpse generate-potion]]
    [rlserver.generate.pc :refer [generate-pc]]))


(defn midpoint [[[x y] w h]]
  [(int (+ x (/ w 2))) (int (+ y (/ h 2)))])

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

(defn generate-corridors [room-defs]
  (loop [remaining-rooms room-defs
         corridors '()]
    (if (empty? remaining-rooms) corridors
                                 (recur (rest (rest remaining-rooms))
                                        (conj corridors [(midpoint (first remaining-rooms))
                                                         (midpoint (second remaining-rooms))])))))

(defn generate-in-room [state room nmax generator]
  (apply-times state nmax #(generator % (rand-coll room))))

(defn generate-all [state rooms nmax generator]
  (apply-seq state rooms #(generate-in-room %1 %2 nmax generator)))

(defn generate-level [state lvlid]
  (set-seed! lvlid)
  (let [[x y] [48 48]
        size [x y]
        t1-defs (->> [[1 1] (- x 2) (- y 2)]
                     (bsp-partition))
        t2-defs (->> t1-defs
                     (map bsp-partition)
                     (apply concat))
        t3-defs (->> t2-defs
                     (map bsp-partition)
                     (apply concat))
        t4-defs (->> t3-defs
                     (map bsp-partition)
                     (apply concat))

        corridor-defs (concat (generate-corridors t1-defs)
                              (generate-corridors t2-defs)
                              (generate-corridors t3-defs)
                              (generate-corridors t4-defs))

        corridor-fields (map (fn [[p1 [x2 y2]]] (rect p1 [(inc x2) (inc y2)])) corridor-defs)
        room-fields (map (fn [[[x y] w h]] (rect [x y] w h 2)) t4-defs)
        rect-fields (concat corridor-fields room-fields)

        open-fields (distinct (apply concat rect-fields))]
    (-> state
        (assoc :level lvlid)
        (assoc :mapsize size)
        (assoc :maphash (hash open-fields))
        (assoc :open open-fields)
        (assoc :biome (rand-coll [:frost :castle :ruin]))

        (generate-stair-up (rand-coll (first room-fields)) (dec lvlid))
        (generate-stair-down (rand-coll (last room-fields)) (inc lvlid))

        (generate-pc 0 (rand-coll (first room-fields)))
        (generate-pc 1 (rand-coll (first room-fields)))

        (generate-all (rest room-fields) 2 generate-bat)
        (generate-all (rest room-fields) 1 generate-skeleton)
        (generate-all (rest room-fields) 1 generate-potion)
        )))
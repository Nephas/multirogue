(ns rlserver.generate.level
  (:require [rlserver.generate.npc :refer [generate-bat generate-skeleton]]
            [rlserver.generate.object :refer [generate-stair-up generate-stair-down generate-corpse generate-blood]]
            [rlserver.generate.pc :refer [generate-pc]]))

(defn rand-range
  [n1 n2] (+ n1 (rand-int (- n2 n1))))

(defn rand-bool []
  (zero? (rand-int 2)))

(defn rect
  ([[x1 y1] [x2 y2] m] (for [x (range (+ x1 m) (- x2 m)) y (range (+ y1 m) (- y2 m))] [x y]))
  ([[x1 y1] [x2 y2]] (for [x (range x1 x2) y (range y1 y2)] [x y]))
  ([[x y] w h m] (rect [x y] [(+ x w) (+ y h)] m)))

(defn midpoint [[[x y] w h]]
  [(int (+ x (/ w 2))) (int (+ y (/ h 2)))])

(defn bsp-partition [[[x y] w h]]
  (let [rand-slice #(int (* % (rand-nth [2/3 3/5 2/5 1/3])))]
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

(defn generate-level [state lvlid]
  (let [[x y] [48 48]
        size [x y]
        mapseed (rand-int 999999)
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
        room-fields (map (fn [[[x y] w h]] (rect [x y] (dec w) (dec h) 1)) t4-defs)
        rect-fields (concat corridor-fields room-fields)
        ]
    (-> state
        (assoc :level lvlid)
        (assoc :mapsize size)
        (assoc :mapseed mapseed)
        (assoc :open (distinct (apply concat rect-fields)))
        (assoc :biome (rand-nth [:frost :castle :ruin]))
        (generate-stair-up (rand-nth (first room-fields)) (dec lvlid))
        (generate-stair-down (rand-nth (last room-fields)) (inc lvlid))
        (generate-pc 0 (rand-nth (first room-fields)))
        (generate-pc 1 (rand-nth (first room-fields)))
        (generate-bat (rand-nth (second room-fields)))
        (generate-bat (rand-nth (second room-fields)))
        (generate-skeleton (rand-nth (nth room-fields 3))))))
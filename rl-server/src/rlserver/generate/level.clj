(ns rlserver.generate.level
  (:require [rlserver.generate.npc :refer [generate-npc]]))

(def NROOMS [6 8])
(def ROOMSIZE [6 12])

(defn rand-range
  [n1 n2] (+ n1 (rand-int (- n2 n1))))

(defn rect
  ([[x1 y1] [x2 y2]] (for [x (range x1 x2) y (range y1 y2)] [x y]))
  ([[x y] w h] (rect [x y] [(+ x w) (+ y h)])))

(defn rand-room [[xmax ymax]]
  (rect [(rand-range 0 (- xmax 12)) (rand-range 0 (- ymax 12))]
        (apply rand-range ROOMSIZE) (apply rand-range ROOMSIZE)))

(defn generate-level [state size]
  (println size)
  (println (rand-room size))
  (let [rooms-fields [(rect [4 4] [11 11])
                      (rand-room size)
                      (rand-room size)
                      (rand-room size)
                      ]]
    (-> state
        (assoc :mapsize size)
        (assoc :open (distinct (apply concat rooms-fields)))
        (generate-npc [10 10])
        (generate-npc [11 11])
        (generate-npc [10 11])
        (generate-npc [11 10])
        )))
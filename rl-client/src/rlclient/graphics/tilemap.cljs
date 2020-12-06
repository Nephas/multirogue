(ns rlclient.graphics.tilemap
  (:require [rlclient.graphics.sheets :as t]))

(def tilemap
  {:floor [[1 0] [2 0] [3 0] [4 0]
           [1 2] [2 2] [3 2] [4 2]
           [1 3] [2 3] [3 3] [4 3]
           [1 4] [3 4] [4 4]]})

(defn get-floortile [seed]
  (let [floors (:floor tilemap)
        len (count floors)
        [i j] (nth floors (mod (hash seed) len))]
    (t/get-tile i j)))

(defn get-walltile []
  (t/get-tile 2 4))
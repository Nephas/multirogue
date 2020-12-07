(ns rlclient.graphics.tilemap
  (:require [rlclient.graphics.sheets :as t]))

(def tilemap
  {:floor [[1 0] [2 0] [3 0] [4 0]
           [1 2] [2 2] [3 2] [4 2]
           [1 3] [2 3] [3 3] [4 3]
           [1 4] [3 4] [4 4]]

   :wall  {:horizontal     [[1 1] [2 1] [3 1] [4 1]]
           :vertical-left  [[5 1] [5 2] [5 3]]
           :vertical-right [[0 1] [0 2] [0 3]]
           :solid          [[2 4]]
           :darkness       [[0 0]]
           :inner-corner   {:right-up [[0 4]]
                            :left-up  [[5 4]]}
           :outer-corner   {:right-up [[4 6]]
                            :left-up  [[1 6]]}}})


(defn rand-nth [coll seed]
  (get coll (mod seed (count coll))))

(defn offset [[x1 y1] [x2 y2]]
  [(+ x1 x2) (+ y1 y2)])

(defn rect [[x1 y1] [x2 y2]]
  (for [x (range x1 x2) y (range y1 y2)] [x y]))

(defn cache-map [positions constructor]
  (->> positions
       (map (fn [pos] [pos (constructor pos)]))
       (apply concat)
       (apply hash-map)))

(defn with-biome [pos biome]
  (let [biome-offset {:castle [0 0]
                      :frost  [0 7]
                      :ruin   [0 14]}]
    (offset pos (get biome-offset biome))))
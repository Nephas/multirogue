(ns rlclient.graphics.wall
  (:require [clojure.set :as set]
            [rlclient.graphics.tilemap :as tm]
            [rlclient.network.connect :refer [remote-state]]))

(def wallmap (atom nil))

(defn includes? [e arr]
  (some #(= e %) arr))

(defn includes-all? [els arr]
  (set/subset? (set els) (set arr)))

(defn get-wall-facing [open-dirs pos]
  (let [seed (hash pos)
        path (cond
               ;inner corners
               (= [:right-down] open-dirs) [:wall :vertical-right]
               (= [:right-up] open-dirs) [:wall :inner-corner :right-up]
               (= [:left-down] open-dirs) [:wall :vertical-left]
               (= [:left-up] open-dirs) [:wall :inner-corner :left-up]

               ;outer corners
               (includes-all? [:right :right-up :up] open-dirs) [:wall :outer-corner :right-up]
               (includes-all? [:left :left-up :up] open-dirs) [:wall :outer-corner :left-up]

               ;straight walls
               (includes? :up open-dirs) [:wall :horizontal]
               (includes? :down open-dirs) [:wall :horizontal]
               (includes-all? [:left :right] open-dirs) [:wall :solid]
               (includes? :left open-dirs) [:wall :vertical-left]
               (includes? :right open-dirs) [:wall :vertical-right]

               (not (empty? open-dirs)) [:wall :solid]
               true [:wall :darkness])]
    (tm/rand-nth (get-in tm/tilemap path) seed)))

(defn direct-neighborhood [[x y]]
  {:right      [(inc x) y]
   :right-down [(inc x) (inc y)]
   :right-up   [(inc x) (dec y)]
   :left       [(dec x) y]
   :left-down  [(dec x) (inc y)]
   :left-up    [(dec x) (dec y)]
   :down       [x (inc y)]
   :up         [x (dec y)]})

(defn get-walltile [open-positions pos]
  (let [neighbors (direct-neighborhood pos)
        open-neighbors (set/intersection (set open-positions) (set (vals neighbors)))
        open-dirs (into [] (vals (select-keys (set/map-invert neighbors) open-neighbors)))]
    (get-wall-facing open-dirs pos)))

(defn cache-walls [biome]
  (when (and (nil? @wallmap) (not (nil? @remote-state)))
    (let [{mapsize    :mapsize
           open-positions :open} @remote-state
          positions (tm/rect [0 0] mapsize)
          wall-positions (set/difference (set positions) (set open-positions))]
      (reset! wallmap
              (tm/cache-map wall-positions
                            #(tm/with-biome (get-walltile open-positions %) biome))))))
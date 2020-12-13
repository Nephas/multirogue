(ns rlclient.graphics.wall
  (:require [clojure.set :as set]
            [rlclient.graphics.tilemap :as tm]
            [rllib.board :refer [large-neighborhood rect]]
            [rlclient.network.connect :refer [remote-state]]))

(def wallmap (atom {:maphash nil
                    :tiles   []}))

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

               (includes-all? [:up :left :right] open-dirs) [:wall :solid]
               (includes-all? [:up :down] open-dirs) [:wall :horizontal]

               ;outer corners
               (includes-all? [:right :right-up :up] open-dirs) [:wall :outer-corner :right-up]
               (includes-all? [:left :left-up :up] open-dirs) [:wall :outer-corner :left-up]
               (includes-all? [:right-down :right-up :up] open-dirs) [:wall :outer-corner :right-up]
               (includes-all? [:left-down :left-up :up] open-dirs) [:wall :outer-corner :left-up]

               ;straight walls
               (includes? :up open-dirs) [:wall :horizontal]
               (includes? :down open-dirs) [:wall :horizontal]
               (includes-all? [:left :right] open-dirs) [:wall :solid]
               (includes? :left open-dirs) [:wall :vertical-left]
               (includes? :right open-dirs) [:wall :vertical-right]

               (not (empty? open-dirs)) [:wall :solid]
               true [:wall :darkness])]
    (tm/rand-nth (get-in tm/tilemap path) seed)))

(defn get-walltile [open-positions pos]
  (let [neighbors (large-neighborhood pos)
        open-neighbors (set/intersection (set open-positions) (set (vals neighbors)))
        open-dirs (into [] (vals (select-keys (set/map-invert neighbors) open-neighbors)))]
    (get-wall-facing open-dirs pos)))

(defn cache-walls []
  (when (and (not (nil? @remote-state)) (not= (:maphash @wallmap) (:maphash @remote-state)))
    (let [{mapsize        :mapsize
           open-positions :open
           biome          :biome
           maphash        :maphash} @remote-state
          positions (rect [0 0] mapsize)
          wall-positions (set/difference (set positions) (set open-positions))
          cached-tiles (tm/cache-map wall-positions
                              #(tm/with-biome (get-walltile open-positions %) biome))]
      (reset! wallmap
              {:maphash maphash
               :tiles   cached-tiles}))))
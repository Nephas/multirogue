(ns rlclient.graphics.floor
  (:require [rlclient.graphics.tilemap :as tm]
            [rllib.board :refer [large-neighborhood rect]]
            [rlclient.network.connect :refer [remote-state]]))

(def floormap (atom nil))

(defn cache-floors []
  (when (and (some? @remote-state) (not= (:maphash @floormap) (:maphash @remote-state)))
    (let [{open    :open
           mapsize :mapsize
           biome   :biome
           maphash :maphash} @remote-state
          floors (:floor tm/tilemap)
          open-positions (filter (fn [pos] (not (zero? (get-in open pos))))
                                 (rect [0 0] mapsize))
          cached-tiles (tm/cache-map open-positions
                                     #(tm/with-biome (tm/rand-nth floors (hash %)) biome))]
      (reset! floormap
              {:maphash maphash
               :tiles   cached-tiles}))))
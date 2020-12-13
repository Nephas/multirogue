(ns rlclient.graphics.floor
  (:require [rlclient.graphics.tilemap :as tm]
            [rlclient.network.connect :refer [remote-state]]))

(def floormap (atom nil))

(defn cache-floors []
  (when (and (not (nil? @remote-state)) (not= (:maphash @floormap) (:maphash @remote-state)))
    (let [{open-positions :open
           biome          :biome
           maphash        :maphash} @remote-state
          floors (:floor tm/tilemap)
          cached-tiles (tm/cache-map open-positions
                              #(tm/with-biome (tm/rand-nth floors (hash %)) biome))]
      (reset! floormap
              {:maphash maphash
               :tiles   cached-tiles}))))
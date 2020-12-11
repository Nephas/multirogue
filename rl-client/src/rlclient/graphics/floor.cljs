(ns rlclient.graphics.floor
  (:require [rlclient.graphics.tilemap :as tm]
            [rlclient.network.connect :refer [remote-state]]))

(def floormap (atom nil))

(defn cache-floors [biome]
  (when (and (nil? @floormap) (not (nil? @remote-state)))
    (let [{open-positions :open} @remote-state
          floors (:floor tm/tilemap)]
      (reset! floormap
              (tm/cache-map open-positions
                            #(tm/with-biome (tm/rand-nth floors (hash %)) biome))))))
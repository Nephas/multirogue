(ns rlclient.graphics.camera
  (:require [quil.core :as q]
            [rllib.vector :refer [add sub scalar]]
            [rlclient.network.connect :as s]
            [rlclient.network.session :as r]))

(def SIZE 8)
(def SCALE 3)
(def MAPRES [1024 800])
(def MAPSIZE (scalar (/ 1 SCALE SIZE) MAPRES))

(def camera (atom [0 0]))

(defn draw-at [img pos]
  (let [[x y] (scalar (* SCALE SIZE) (add pos @camera))]
    (q/image img x y)))

(defn draw-fixed [img pos]
  (let [[x y] (scalar (* SCALE SIZE) pos)]
    (q/image img x y)))

(defn update-camera []
  (let [[off-x off-y] MAPSIZE
        [x y] (get-in @s/remote-state [:pos (r/player-id)])]
    (reset! camera [(- (/ off-x 2) x) (- (/ off-y 2) y)])))
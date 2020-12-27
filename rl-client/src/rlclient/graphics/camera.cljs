(ns rlclient.graphics.camera
  (:require [quil.core :as q]
            [rllib.vector :refer [add sub scalar]]
            [rlclient.network.connect :as s]
            [rlclient.network.session :as r]))

(def SIZE 24)
(def SCALE 1)
(def MAPRES [1024 800])
(def MAPSIZE (scalar (/ 1 SCALE SIZE) MAPRES))

(def camera (atom [0 0]))
(def cursor (atom [0 0]))

(defn at-mappos
  ([[x y]] (scalar (* SCALE SIZE) (add [x y] @camera)))
  ([img pos] (let [[x y] (at-mappos pos)]
               (q/image img x y))))

(defn draw-fixed [img pos]
  (let [[x y] (scalar (* SCALE SIZE) pos)]
    (q/image img x y)))

(defn update-camera []
  (let [playerpos (get-in @s/remote-state [:pos (r/player-id)])]
    (reset! camera (-> MAPSIZE
                       (#(scalar 0.5 %))
                       (sub playerpos)))))

(defn update-cursor []
  (reset! cursor (-> [(q/mouse-x) (q/mouse-y)]
                     (#(scalar (/ 1 SIZE) %))
                     (sub @camera)
                     (#(mapv int %)))))
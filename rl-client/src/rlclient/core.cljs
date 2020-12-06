(ns rlclient.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [rlclient.input :refer [handle-key]]
            [rlclient.network.connect :as s]
            [rlclient.network.session :as r]
            [rlclient.graphics.sheets :as sh]
            [rlclient.graphics.tilemap :as tm]))

(defn setup []
  (s/connect-socket)
  (sh/fetch-tileset)
  (sh/fetch-animations)
  (q/color-mode :hsb 1.0)
  (q/frame-rate 10))


(defn update-state [state]
  (sh/slice-tileset))

(defn draw-state [state]
  (when-not (or (nil? @sh/tiles) (nil? @sh/animations))
    (let [state @s/remote-state]
      (q/background 128)

      (doseq [i (range 0 24)
              j (range 0 24)]
        (sh/draw-at (tm/get-walltile) [i j]))

      (doseq [x (:open state)]
        (sh/draw-at (tm/get-floortile x) x))

      (doseq [k (keys (:animated state))]
        (sh/draw-at (sh/get-animation (get (:animated state) k))
                    (get (:pos state) k)))

      (q/fill 1.0)
      (q/text (str "Player: " (r/player-id)) 10 20)
      )))

(defn ^:export run-sketch []
  (q/defsketch rl-client
               :host "rl-client"
               :size [1024 1024]
               :setup setup
               :update update-state
               :draw draw-state
               :key-pressed handle-key

               :middleware [m/fun-mode]))
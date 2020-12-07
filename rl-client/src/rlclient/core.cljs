(ns rlclient.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [rlclient.input :refer [handle-key]]
            [rlclient.network.connect :as s]
            [rlclient.network.session :as r]
            [rlclient.graphics.sheets :as sh]
            [rlclient.graphics.wall :as w]
            [rlclient.graphics.floor :as f]
            [rlclient.graphics.sheets :as sh]
            ))

(defn setup []
  (s/connect-socket!)
  (sh/fetch-tileset)
  (sh/fetch-animations)
  (q/color-mode :hsb 1.0)
  (q/frame-rate 8))


(defn update-state [state]
  (sh/slice-tileset)
  (w/cache-walls :castle)
  (f/cache-floors :castle))

(defn draw-state [state]
  (when-not (or (nil? @sh/tiles) (nil? @sh/animations))
    (let [state @s/remote-state]
      (q/background 128)

      ;walls
      (doseq [[pos tile] @w/wallmap]
        (sh/draw-at (sh/get-tile tile) pos))

      ;floors
      (doseq [[pos tile] @f/floormap]
        (sh/draw-at (sh/get-tile tile) pos))

      ;entities
      (doseq [k (keys (:animated state))]
        (sh/draw-at (sh/get-animation (get (:animated state) k) (hash k))
                    (get (:pos state) k)))

      (q/fill 1.0)
      (q/text (str "Player: " (r/player-id)) 10 20)
      (q/text (str "Tic: " (:tic state)) 10 40)
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
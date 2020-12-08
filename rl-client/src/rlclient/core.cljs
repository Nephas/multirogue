(ns rlclient.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [rlclient.input :refer [handle-event refire-key]]
            [rlclient.network.connect :as s]
            [rlclient.network.session :as r]
            [rlclient.graphics.sheets :as sh]
            [rlclient.graphics.camera :as c]
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
  (refire-key)
  (sh/slice-tileset)
  (w/cache-walls :castle)
  (f/cache-floors :castle))

(defn draw-state [state]
  (when-not (or (nil? @sh/tiles) (nil? @sh/animations))
    (c/update-camera)
    (q/background 0)
    (let [state @s/remote-state]

      ;walls
      (doseq [[pos tile] @w/wallmap]
        (c/draw-at (sh/get-tile tile) pos))

      ;floors
      (doseq [[pos tile] @f/floormap]
        (c/draw-at (sh/get-tile tile) pos))

      ;sprites
      (doseq [[id tile] (:sprite state)]
        (c/draw-at (sh/get-tile tile)
                    (get (:pos state) id)))

      ;animations
      (doseq [[id animation] (:animated state)]
        (c/draw-at (sh/get-animation animation (hash id))
                    (get (:pos state) id)))

      ;animations
      (doseq [[id animation] (:effect state)]
        (c/draw-at (sh/get-animation animation (hash id))
                    (get (:pos state) id)))

      (q/fill 1.0)
      (q/text (str "Player: " (r/player-id)) 10 20)
      (q/text (str "Tic: " (:tic state)) 10 40)
      )))

(defn ^:export run-sketch []
  (q/defsketch rl-client
               :host "rl-client"
               :size c/MAPRES
               :setup setup
               :update update-state
               :draw draw-state
               :key-pressed handle-event

               :middleware [m/fun-mode]))
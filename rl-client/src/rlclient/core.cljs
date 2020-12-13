(ns rlclient.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [rlclient.input.input :refer [handle-event refire-key]]
            [rlclient.network.connect :as s]
            [rlclient.network.session :as r]
            [rlclient.graphics.sheets :as sh]
            [rlclient.graphics.gui.bar :as b]
            [rlclient.graphics.camera :as c]
            [rlclient.graphics.wall :as w]
            [rlclient.graphics.floor :as f]
            [rlclient.graphics.sheets :as sh]
            [rllib.constant :refer [FRAMERATE]]
            ))

(defn setup []
  (s/connect-socket!)
  (sh/fetch-tileset)
  (sh/fetch-animations)

  (q/text-font (q/load-font "/font/heorot.ttf"))
  (q/color-mode :hsb 1.0)
  (q/frame-rate FRAMERATE))

(defn update-state [state]
  (refire-key)
  (sh/slice-tileset)
  (w/cache-walls)
  (f/cache-floors))

(defn draw-state [state]
  (when-not (or (nil? @sh/tiles) (nil? @sh/animations))
    (c/update-camera)
    (q/background 0)
    (let [state @s/remote-state]

      ;walls
      (doseq [[pos tile] (:tiles @w/wallmap)]
        (c/draw-at (sh/get-tile tile) pos))

      ;floors
      (doseq [[pos tile] (:tiles @f/floormap)]
        (c/draw-at (sh/get-tile tile) pos))

      ;sprites
      (doseq [[id tile] (:sprite state)]
        (c/draw-at (sh/get-tile tile)
                   (get (:pos state) id)))

      ;animations
      (doseq [[id animation] (:animated state)]
        (c/draw-at (sh/get-animation animation (hash id))
                   (get (:pos state) id)))

      ;effect
      (doseq [[id effect] (:effect state)]
        (c/draw-at (sh/get-animation effect (hash id))
                   (get (:pos state) id)))

      (q/fill 0)
      (q/rect 0 0 175 175)
      (q/fill 1.0)
      (q/text-size 20)
      (q/text (str "Player: " (r/player-id)) 30 120)
      (q/text (str "Tic: " (:tic state)) 30 150)
      (q/text (str "Level: " (:level state)) 30 180)

      (b/red-bar [1 1] (get-in state [:hp (r/player-id)]))
      (b/green-bar [1 2] (get-in state [:ap (r/player-id)])))))

(defn ^:export run-sketch []
  (q/defsketch rl-client
               :host "rl-client"
               :size c/MAPRES
               :setup setup
               :update update-state
               :draw draw-state
               :key-pressed handle-event

               :middleware [m/fun-mode]))
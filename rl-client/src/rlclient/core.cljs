(ns rlclient.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [rlclient.input.input :refer [handle-event refire-key]]
            [rlclient.network.connect :as s]
            [rlclient.render.world :refer [render-world!]]
            [rlclient.render.loading :refer [render-loadscreen!]]
            [rlclient.graphics.sheets :as sh]
            [rlclient.graphics.camera :as c]
            [rlclient.graphics.wall :as w]
            [rlclient.graphics.floor :as f]
            [rlclient.render.fov :as fov]
            [rlclient.graphics.sheets :as sh]
            [rllib.constant :refer [FRAMERATE]]
            ))

(defn setup []
  (s/connect-socket!)
  (sh/fetch-images)
  (sh/fetch-sheets)

  (q/no-smooth)
  (q/text-size 20)
  (q/text-font (q/load-font "/font/heorot.ttf"))
  (q/color-mode :hsb 1.0)
  (q/frame-rate FRAMERATE))

(defn update-state [state]
  (sh/slice-sheets)
  (refire-key)
  (w/cache-walls)
  (f/cache-floors)
  (fov/update-fovmap))

(defn draw-state [state]
  (let [state @s/remote-state]
    (when (and (not (neg? (:load state))) (sh/image-loaded (:load state)))
      (render-loadscreen! (:load state)))
    (when (and (neg? (:load state)) (some? @sh/tiles) (some? @sh/animations))
      (render-world! @s/remote-state))))

(defn ^:export run-sketch []
  (q/defsketch rl-client
               :host "rl-client"
               :size c/MAPRES
               :setup setup
               :update update-state
               :draw draw-state
               :key-pressed handle-event
               :mouse-moved c/update-cursor

               :middleware [m/fun-mode]))
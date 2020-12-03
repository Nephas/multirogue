(ns rl-client.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [rl-client.graphics.tiles :as t]))

(defn setup []
  (t/fetch-tileset)
  (q/color-mode :hsb)
  (q/frame-rate 30))


(defn update-state [state]
  (t/slice-tileset))

(defn draw-state [state]
  (when-not (nil? @t/tiles)
    (q/background 128)

    (doseq [i (range 0 32)
            j (range 0 24)]
      (t/draw-at (t/get-tile i j) (mapv #(* 1.2 %) [i j])))))

(defn ^:export run-sketch []
  (q/defsketch rl-client
               :host "rl-client"
               :size [500 500]
               :setup setup
               :update update-state
               :draw draw-state
               :middleware [m/fun-mode]))
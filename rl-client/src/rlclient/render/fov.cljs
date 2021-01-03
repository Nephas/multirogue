(ns rlclient.render.fov
  (:require [rllib.board :refer [rect-array rect circle]]
            [rllib.vector :refer [add sub]]
            [rllib.state :refer [apply-seq]]
            [rlclient.network.connect :refer [remote-state]]
            [rlclient.network.session :refer [player-id]]))

(def fovmap (atom {:tic nil
                   :fov nil}))

(defn player-fov [fovmap]
  (let [pos (get-in @remote-state [:pos (player-id)])
        explored (circle pos 15)
        infov (circle pos 10)]
    (-> fovmap
        (apply-seq explored (fn [fm p] (assoc-in fm p 0)))
        (apply-seq infov (fn [fm p] (assoc-in fm p 1))))))
(defn update-fov-map []
  (when (and (some? @remote-state) (or (nil? (:fov @fovmap)) (not= (:tic @fovmap) (:tic @remote-state))))
    (reset! fovmap {:tic (:tic @remote-state)
                    :fov (-> (:mapsize @remote-state)
                             (rect-array -1)
                             (player-fov))})))

(defn in-fov? [pos]
  (pos? (get-in @fovmap (cons :fov pos))))

(defn explored? [pos]
  (zero? (get-in @fovmap (cons :fov pos))))
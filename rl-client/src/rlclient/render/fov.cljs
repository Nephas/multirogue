(ns rlclient.render.fov
  (:require [rllib.board :refer [rect-array rect circle]]
            [rllib.vector :refer [add sub]]
            [rllib.state :refer [apply-seq]]
            [rlclient.network.connect :refer [remote-state]]
            [rlclient.network.session :refer [player-id]]))

(def fovmap (atom {:tic   nil
                   :level nil
                   :fov   nil}))

(defn memorize-fov [fovmap]
  (let [memorize #(if (neg? %) -1 0)]
    (into [] (map #(into [] (map memorize %)) fovmap))))

(defn in-map? [[sizex sizey] [x y]]
  (and (< 0 x sizex) (< 0 y sizey)))

(defn cast-fov [fovmap]
  (let [pos (get-in @remote-state [:pos (player-id)])
        infov (filter #(in-map? (:mapsize @remote-state) %) (circle pos 12))]
    (apply-seq fovmap infov (fn [fm p] (assoc-in fm p 1)))))

(defn update-player-fov []
  (do (swap! fovmap assoc :tic (:tic @remote-state))
      (swap! fovmap update :fov memorize-fov)
      (swap! fovmap update :fov cast-fov)))

(defn init-fovmap []
  (reset! fovmap {:tic (:tic @remote-state)
                  :level (:level @remote-state)
                  :fov (-> (:mapsize @remote-state)
                           (rect-array -1)
                           (cast-fov))}))

(defn update-fovmap []
  (when (and (some? @remote-state) (or (nil? (:fov @fovmap)) (not= (:level @fovmap) (:level @remote-state))))
    (init-fovmap))
  (when (and (some? (:fov @fovmap)) (not= (:tic @fovmap) (:tic @remote-state)))
    (update-player-fov)))

(defn in-fov? [pos]
  (pos? (get-in @fovmap (cons :fov pos))))

(defn explored? [pos]
  (zero? (get-in @fovmap (cons :fov pos))))
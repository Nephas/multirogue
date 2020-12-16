(ns rlclient.graphics.sheets
  (:require [quil.core :as q :include-macros true]
            [rlclient.graphics.camera :refer [SIZE SCALE]]))

(def tilesheet (atom nil))
(def animationsheet (atom nil))

(def tiles (atom nil))
(def animations (atom nil))

(defn get-tile
  ([x y] (nth @tiles (+ y (* x 22))))
  ([[x y]] (get-tile x y)))

(defn get-animation [n seed]
  (let [fr (mod (+ seed (q/frame-count)) 5)]
    (nth @animations (+ n (* fr 8)))))

(defn get-dims [img]
  [(/ (. img -width) SIZE) (/ (. img -height) SIZE)])

(defn slice [sheet]
  (let [[cols rows] (get-dims sheet)]
    (println "slicing sheet into" cols "x" rows)
    (for [x (range cols)
          y (range rows)]
      (let [gr (quil.core/create-image SIZE SIZE)]
        (q/copy sheet gr [(* x SIZE) (* y SIZE) SIZE SIZE] [0 0 SIZE SIZE])
        (q/resize gr (* SCALE SIZE) (* SCALE SIZE))
        gr))))

(defn slice-sheets []
  (when (and (nil? @tiles) (quil.core/loaded? @tilesheet))
    (reset! tiles (slice @tilesheet)))
  (when (and (nil? @animations) (quil.core/loaded? @animationsheet))
    (reset! animations (slice @animationsheet))))

(defn fetch-sheets []
  (let [origin (. (. js/document -location) -origin)]
    (do (print "fetching graphics sheets")
        (reset! tilesheet (q/load-image (str origin "/img/tileset.png")))
        (reset! animationsheet (q/load-image (str origin "/img/animations.png"))))))
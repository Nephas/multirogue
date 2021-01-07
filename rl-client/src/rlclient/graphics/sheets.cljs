(ns rlclient.graphics.sheets
  (:require [quil.core :as q :include-macros true]
            [rlclient.graphics.camera :refer [SIZE SCALE]]))

(def tilesheet (atom nil))
(def animationsheet (atom nil))
(def image (atom {}))

(def tiles (atom nil))
(def animations (atom nil))

(defn get-tile
  ([x y] (nth @tiles (+ y (* x 22))))
  ([[x y]] (get-tile x y)))

(defn get-animation [n seed]
  (let [fr (mod (+ seed (q/frame-count)) 5)]
    (nth @animations (+ n (* fr 16)))))

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
        (reset! tilesheet (q/load-image (str origin "/img/tileset_" SIZE ".png")))
        (reset! animationsheet (q/load-image (str origin "/img/animations_" SIZE ".png"))))))

(defn fetch-images []
  (let [origin (. (. js/document -location) -origin)]
    (do (print "fetching images")
        (swap! image assoc 0 (q/load-image (str origin "/img/image_0.png")))
        (swap! image assoc 1 (q/load-image (str origin "/img/image_1.png")))
        (swap! image assoc 2 (q/load-image (str origin "/img/image_1.png"))))))

(defn image-loaded [index]
  (and (some? (get @image index)) (quil.core/loaded? (get @image index))))
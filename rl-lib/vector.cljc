(ns rllib.vector)

(defn abs [x]
  (if (neg? x) (- x) x))

(defn add [v1 v2]
  (mapv + v1 v2))

(defn sub [v1 v2]
  (mapv - v1 v2))

(defn scalar [a [x y]]
  [(* a x) (* a y)])

(defn man-dist [[x1 y1] [x2 y2]]
   (+ (abs (- x1 x2)) (abs (- y1 y2))))
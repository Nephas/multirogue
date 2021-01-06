(ns rllib.vector)

(defn abs [x]
  (if (neg? x) (- x) x))

(defn sign [x]
  (cond (pos? x) 1
        (zero? x) 0
        (neg? x) -1))

(defn add [v1 v2]
  (mapv + v1 v2))

(defn sub [v1 v2]
  (mapv - v1 v2))

(defn scalar [a [x y]]
  [(* a x) (* a y)])

(defn man-dist [[x1 y1] [x2 y2]]
  (+ (abs (- x1 x2)) (abs (- y1 y2))))

(defn diag-dist [p1 p2]
  (let [[dx dy] (sub p2 p1)]
    (max (abs dx) (abs dy))))

(defn round [x]
  (int (if (>= (rem x 1) 0.5) (inc x) x)))
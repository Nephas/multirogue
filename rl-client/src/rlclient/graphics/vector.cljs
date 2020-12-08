(ns rlclient.graphics.vector)

(defn add [v1 v2]
  (mapv + v1 v2))

(defn sub [v1 v2]
  (mapv - v1 v2))

(defn scalar [a [x y]]
  [(* a x) (* a y)])
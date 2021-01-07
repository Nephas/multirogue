(ns rlclient.render.shadowcast
  (:require [rllib.vector :refer [add abs sign sub diag-dist round]]
            [rllib.board :refer [rect]]
            [rllib.state :refer [apply-seq]]
            [rlclient.graphics.wall :refer [block-los?]]))

(defn lerp [x0 x1 t]
  (+ x0 (* t (- x1 x0))))

(defn line [[x0 y0] [x1 y1]]
  (let [dist (diag-dist [x0 y0] [x1 y1])
        steps (map #(/ % dist) (range (inc dist)))]
    (map #(vector (round (lerp x0 x1 %)) (int (lerp y0 y1 %))) steps)))

(defn raymap
  ([r] (let [up (map #(vector % r) (range (- r) r))
             down (map #(vector % (- r)) (range (- r) r))
             left (map #(vector (- r) %) (range (- r) r))
             right (map #(vector r %) (range (- r) r))
             close-enough? #(let [[dx dy] (sub [0 0] %)]
                              (< (+ (* dx dx) (* dy dy)) (* r r)))]
         (->> (concat up right down left)
              (distinct)
              (map #(line [0 0] %))
              (map #(filter close-enough? %))
              (map rest)))))

(def PLAYER_FOV (raymap 16))

(defn offset [raymap pos]
  (let [offset #(add pos %)]
    (map #(map offset %) raymap)))

(defn light-up [fovmap [x y]]
  (let [neighbors (list [(inc x) y] [(dec x) y] [x (inc y)] [x (dec y)])]
    (apply-seq fovmap (conj (filter block-los? neighbors) [x y])
               (fn [s p] (assoc-in s p 1)))))

(defn cast-ray [fovmap ray]
  (loop [[pos & remaining] ray
         current fovmap]
    (cond (nil? pos) current
          (block-los? pos) (assoc-in current pos 1)
          true (recur remaining
                      (light-up current pos)))))

(defn cast-raymap [fovmap raymap origin]
  (time (loop [[ray & remaining] (offset raymap origin)
               current fovmap]
          (if (nil? ray) current
                         (recur remaining
                                (cast-ray current ray))))))
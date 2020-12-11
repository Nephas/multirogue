(ns rlserver.reducer.move
  (:require [rlserver.system.action :refer [active? spend-ap]]))

(defn move [[x y] dir]
  (cond (= :up dir) [x (dec y)]
        (= :down dir) [x (inc y)]
        (= :left dir) [(dec x) y]
        (= :right dir) [(inc x) y]))

(defn open? [state pos]
  (let [open-pos (:open state)
        blocked-pos (vals (select-keys (:pos state) (:blocking state)))
        open (some #(= pos %) open-pos)
        blocked (some #(= pos %) blocked-pos)]
    (and open (not blocked))))

(defn moveable? [state direction pid]
  (let [origin (get-in state [:pos pid])
        target (move origin direction)]
    (open? state target)))

(defn entity-move [state id dir]
  (-> state
      (update-in [:pos id] move dir)
      (spend-ap id 1)))
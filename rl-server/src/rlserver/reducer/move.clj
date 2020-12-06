(ns rlserver.reducer.move)

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

(defn move-entity [state id dir]
  (update-in state [:pos id] move dir))
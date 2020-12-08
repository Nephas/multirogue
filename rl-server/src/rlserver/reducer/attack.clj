(ns rlserver.reducer.attack
  (:require [rlserver.reducer.move :refer [move]]
            [rlserver.generate.effect :refer [generate-effect]]))

(defn get-entities-at [state target-pos]
  (->> (get state :pos)
       (filter (fn [[id pos]] (= pos target-pos)))
       (map (fn [[id pos]] id))))

(defn damage [state target-pos dam]
  (let [id (first (get-entities-at state target-pos))]
    (-> state (update-in [:hp id 0] - dam))))

(defn entity-attack [state id dir]
  (let [pos (get-in state [:pos id])
        target-pos (move pos dir)]
    (-> state
        (generate-effect target-pos 4)
        (damage target-pos 2))))

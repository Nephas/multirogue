(ns rlserver.reducer.attack
  (:require [rlserver.system.action :refer [spend-ap]]
            [rllib.board :refer [move]]
            [rllib.selector :refer [get-entities-at]]
            [rlserver.generate.effect :refer [generate-effect]]))

(defn damage [state target-pos dam]
  (let [id (first (get-entities-at state target-pos :hp))]
    (if (some? id)
      (update-in state [:hp id 0] - dam)
      state)))

(defn entity-attack [state id dir]
  (let [pos (get-in state [:pos id])
        target-pos (move pos dir)]
    (-> state
        (generate-effect target-pos 4)
        (damage target-pos 1)
        (spend-ap id 1))))

(ns rlserver.reducer.move
  (:require [rlserver.system.action :refer [active? spend-ap]]
            [rllib.board :refer [move]]))

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
  (let [cost (if (contains? (set (:pc state)) id) 1 2)]
    (-> state
        (update-in [:pos id] move dir)
        (spend-ap id cost))))
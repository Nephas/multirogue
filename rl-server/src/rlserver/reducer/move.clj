(ns rlserver.reducer.move
  (:require [rlserver.system.action :refer [active? spend-ap]]
            [rllib.board :refer [move]]))

(defn open? [state [x y]]
  (let [blocked-pos (vals (select-keys (:pos state) (:block state)))
        open? (= 1 (get-in state [:open x y]))
        blocked? (contains? #{[x y]} (set blocked-pos))]
    (and open? (not blocked?))))

(defn moveable? [state direction pid]
  (let [origin (get-in state [:pos pid])
        target (move origin direction)]
    (open? state target)))

(defn entity-move [state id dir]
  (let [cost (if (contains? (set (:pc state)) id) 1 2)]
    (-> state
        (update-in [:pos id] move dir)
        (spend-ap id cost))))
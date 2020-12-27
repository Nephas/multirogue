(ns rlserver.reducer.move
  (:require [rlserver.system.action :refer [active? spend-ap]]
            [rllib.board :refer [move]]
            [rlserver.generate.timer :refer [generate-sprite-effect]]))

(def move-effect {:up    [12 12]
                  :down  [13 12]
                  :right [14 12]
                  :left  [15 12]})

(defn open? [state pos]
  (let [blocked-pos (vals (select-keys (:pos state) (:block state)))
        blocked? (some #(= % pos) blocked-pos)
        open? (= 1 (get-in state (cons :open pos)))]
    (and open? (not blocked?))))

(defn moveable? [state direction pid]
  (let [origin (get-in state [:pos pid])
        target (move origin direction)]
    (open? state target)))

(defn entity-move [state id dir]
  (-> state
      (generate-sprite-effect (get-in state [:pos id]) (get move-effect dir) 1)
      (update-in [:pos id] move dir)))
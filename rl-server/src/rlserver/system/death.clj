(ns rlserver.system.death
  (:require [rlserver.generate.object :refer [generate-corpse]]
            [rlserver.entity.entity :refer [destroy-entity]]
            [rllib.state :refer [apply-seq]]))

(defn alive? [state id]
  (> (get-in state [:hp id 0]) 0))

(defn connected? [state pid]
  (not (contains? (set (:ai state)) pid)))

(defn gameover? [state]
  (->> (:pc state)
       (filter #(connected? state %))
       (filter #(alive? state %))
       (empty?)))

;TODO fix gameover logic
(defn player-die [state id]
  (println (gameover? state))
  (if (gameover? state)
    (assoc state :load 2)
    (-> state
        (update :animated dissoc id)
        (update :follow dissoc id)
        (update :ai #(remove #{id} %))
        (update :block #(remove #{id} %))
        (assoc-in [:sprite id] [8 8]))))

(defn check-death [state id]
  (let [hp (get-in state [:hp id 0])
        pos (get-in state [:pos id])]
    (cond
      (and (<= hp 0) (contains? (set (:pc state)) id))
      (player-die state id)

      (<= hp 0)
      (-> state
          (destroy-entity id)
          (generate-corpse pos))

      true state)))

(defn update-death [game]
  (let [stats (keys (:hp game))]
    (apply-seq game stats check-death)))
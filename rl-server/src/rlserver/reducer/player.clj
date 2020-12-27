(ns rlserver.reducer.player
  (:require [rlserver.system.action :refer [active? spend-ap]]
            [rlserver.generate.pc :refer [other]]
            [rlserver.system.update :as u]
            [rllib.vector :refer [add sub]]
            [rlserver.reducer.move :refer [entity-move moveable?]]
            [rlserver.reducer.attack :refer [entity-attack]]))

(defn handle-action [state action pid]
  (let [[primary secondary] action]
    (cond (= :move primary)
          (if (and (moveable? state secondary pid) (active? state))
            (-> state
                (entity-move pid secondary)
                (spend-ap 1))
            state)

          (= :attack primary)
          (if (active? state)
            (-> state
                (entity-attack pid secondary)
                (spend-ap 2))
            state)

          (= :pass primary)
          (spend-ap state 4)

          (= :continue primary)
          (assoc state :load -1)

          true state)))

(defn resolve-tic [state]
  (-> state
      (u/update-tic)
      (u/maybe-update-ai-turn)))

(defn player-join [state pid]
  (println "\t * player" pid "joined")
  (-> state
      (update :ai #(remove #{pid} %))
      (update :pap #(add % [2 2]))
      (assoc-in [:animated pid] pid)
      (update :follow #(dissoc % pid))))

(defn player-disjoin [state pid]
  (println "\t * player left")
  (-> state
      (update :ai conj pid)
      (update :pap #(sub % [2 2]))
      (assoc-in [:animated pid] (+ pid 2))
      (assoc-in [:follow pid] (other pid))))
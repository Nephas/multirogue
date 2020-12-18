(ns rlserver.reducer.player
  (:require [rlserver.system.action :refer [active?]]
            [rlserver.generate.pc :refer [other]]
            [rlserver.reducer.move :refer [entity-move moveable?]]
            [rlserver.reducer.attack :refer [entity-attack]]))

(defn player-input [state action pid]
  (let [[primary secondary] action]
    (cond (= :pass primary)
          state

          (= :move primary)
          (if (and (moveable? state secondary pid) (active? state pid))
            (entity-move state pid secondary)
            state)

          (= :attack primary)
          (if (active? state pid)
            (entity-attack state pid secondary)
            state)

          true state)))

(defn player-join [state pid]
  (println "\t * player" pid "joined")
  (-> state
      (update :ai #(remove #{pid} %))
      (assoc-in [:animated pid] pid)
      (update :follow #(dissoc % pid))))

(defn player-disjoin [state pid]
  (println "\t * player left")
  (-> state
      (update :ai conj pid)
      (assoc-in [:animated pid] (+ pid 2))
      (assoc-in [:follow pid] (other pid))))
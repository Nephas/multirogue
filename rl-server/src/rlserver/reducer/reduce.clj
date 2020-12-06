(ns rlserver.reducer.reduce
  (:require [rlserver.reducer.move :refer [move-entity moveable?]]))

(defn reduce [state action pid]
  (let [[primary secondary] action]
    (cond (= :pass primary)
          state

          (and (= :move primary) (moveable? state secondary pid))
          (move-entity state pid secondary)

          true state)))
(ns facts)

(defn is-valid-fact?
  "Chequea si es un fact es valido usando regex"
  [fact]
  (not (= nil (re-matches #"^[a-z]+\(([a-z]+, )*[a-z]+\)$" fact))))

(defn fact-name
  "Devuelve el nombre de un fact"
  [fact]
  (subs fact 0 (clojure.string/index-of fact "(")))

(defn exist-fact
  "Chequea que sea el nombre de una fact devuelve true si es asi"
  [database query]
  (let [facts-names (map fact-name (:facts database))]
    (.contains facts-names (fact-name query))
    ))

(defn evaluate-fact
  "Recibe una db y una query (potencialmente fact) y se fija si existe en la base de datos de facts, si existe es verdadera y devuelvo true"
  [database fact]
  (.contains (:facts database) fact)
  )

(defn replace-arg
  ""
  [fact map-args]
  (clojure.string/replace fact (re-pattern (clojure.string/join "|" (keys map-args))) map-args)
  )

(defn replace-args
  "Recibe una lista con facts con argumentos genericos
  y un map con los valores de esos argumentos, devuelve una lista con los facts con
  los argumentos reemplazados"
  [gen-facts map-args]
  (map #( replace-arg % map-args ) gen-facts)
  )
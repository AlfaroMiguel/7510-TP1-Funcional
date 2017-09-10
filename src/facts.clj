(ns facts)

(defn is-valid-fact?
  "Given a fact checks if it's valid"
  [fact]
  (not (= nil (re-matches #"^[a-z]+\(([a-z]+, )*[a-z]+\)$" fact))))

(defn fact-name
  "Given a fact, returns his name"
  [fact]
  (subs fact 0 (clojure.string/index-of fact "(")))

(defn exist-fact
  "Given a database and a query, checks whether the query corresponds to the name of a fact in the database"
  [database query]
  (let [facts-names (map fact-name (:facts database))]
    (.contains facts-names (fact-name query))
    ))

(defn evaluate-fact
  "Receives a database and a fact, it checks if all the fact (with its arguments) is in the database."
  [database fact]
  (.contains (:facts database) fact)
  )

(defn replace-arg
  "Receives a generic fact e.g 'parent (Y, X)', a map with the values ​​of the arguments e.g '{X maría, Y roberto}'
   and replaces the values. It returns the particular fact e.g 'padre(roberto, maría)'"
  [fact map-args]
  (clojure.string/replace fact (re-pattern (clojure.string/join "|" (keys map-args))) map-args)
  )

(defn replace-args
  "Receives a list of generic facts and a map with the values of the arguments, return a list of particulars facts"
  [gen-facts map-args]
  (map #( replace-arg % map-args ) gen-facts)
  )
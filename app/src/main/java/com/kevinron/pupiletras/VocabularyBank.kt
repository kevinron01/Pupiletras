package com.kevinron.pupiletras

object VocabularyBank {
    private val wordDisplayOverrides = mapOf(
        "aguila" to "Águila",
        "aguilas" to "Águilas",
        "arbol" to "Árbol",
        "arboles" to "Árboles",
        "banera" to "Bañera",
        "baneras" to "Bañeras",
        "cancion" to "Canción",
        "canciones" to "Canciones",
        "camion" to "Camión",
        "camiones" to "Camiones",
        "dragon" to "Dragón",
        "dragones" to "Dragones",
        "energia" to "Energía",
        "energias" to "Energías",
        "estomago" to "Estómago",
        "estomagos" to "Estómagos",
        "fantasia" to "Fantasía",
        "fantasias" to "Fantasías",
        "heroe" to "Héroe",
        "heroes" to "Héroes",
        "lapiz" to "Lápiz",
        "lapices" to "Lápices",
        "lampara" to "Lámpara",
        "lamparas" to "Lámparas",
        "limonada" to "Limonada",
        "limonadas" to "Limonadas",
        "melodia" to "Melodía",
        "melodias" to "Melodías",
        "musica" to "Música",
        "nocion" to "Noción",
        "nociones" to "Nociones",
        "oceano" to "Océano",
        "oceanos" to "Océanos",
        "pajaro" to "Pájaro",
        "pajaros" to "Pájaros",
        "pinguino" to "Pingüino",
        "pinguinos" to "Pingüinos",
        "pocion" to "Poción",
        "pociones" to "Pociones",
        "pizarron" to "Pizarrón",
        "pizarrones" to "Pizarrones",
        "rio" to "Río",
        "rios" to "Ríos",
        "tunel" to "Túnel",
        "tuneles" to "Túneles",
        "violin" to "Violín",
        "violines" to "Violines",
        "zoologico" to "Zoológico",
        "zoologicos" to "Zoológicos",
        "carinoso" to "Cariñoso",
        "carinosa" to "Cariñosa",
        "carinosos" to "Cariñosos",
        "carinosas" to "Cariñosas",
        "energico" to "Enérgico",
        "energica" to "Enérgica",
        "energicos" to "Enérgicos",
        "energicas" to "Enérgicas",
        "fantastico" to "Fantástico",
        "fantastica" to "Fantástica",
        "fantasticos" to "Fantásticos",
        "fantasticas" to "Fantásticas",
        "jugueton" to "Juguetón",
        "juguetona" to "Juguetona",
        "juguetones" to "Juguetones",
        "juguetonas" to "Juguetonas",
        "magico" to "Mágico",
        "magica" to "Mágica",
        "magicos" to "Mágicos",
        "magicas" to "Mágicas",
        "pacifico" to "Pacífico",
        "pacifica" to "Pacífica",
        "pacificos" to "Pacíficos",
        "pacificas" to "Pacíficas",
        "rapido" to "Rápido",
        "rapida" to "Rápida",
        "rapidos" to "Rápidos",
        "rapidas" to "Rápidas",
        "risueno" to "Risueño",
        "risuena" to "Risueña",
        "risuenos" to "Risueños",
        "risuenas" to "Risueñas",
        "pequeno" to "Pequeño",
        "pequena" to "Pequeña",
        "pequenos" to "Pequeños",
        "pequenas" to "Pequeñas",
        "curvilineo" to "Curvilíneo",
        "curvilinea" to "Curvilínea",
        "curvilineos" to "Curvilíneos",
        "curvilineas" to "Curvilíneas",
        "ensenar" to "Enseñar",
        "ensenando" to "Enseñando",
        "ensenado" to "Enseñado",
        "senalar" to "Señalar",
        "senalando" to "Señalando",
        "senalado" to "Señalado",
        "reir" to "Reír",
        "riendo" to "Riendo",
        "reido" to "Reído",
        "sonreir" to "Sonreír",
        "sonriendo" to "Sonriendo",
        "sonreido" to "Sonreído",
        "leer" to "Leer",
        "leyendo" to "Leyendo",
        "leido" to "Leído"
    )

    private val words: List<VocabularyWord> by lazy { buildVocabulary() }

    fun allWords(): List<VocabularyWord> = words

    private fun buildVocabulary(): List<VocabularyWord> = buildList {
        addCoreWords()

        addNounSet(
            listOf(
                "abeja", "aguila", "caballo", "canario", "conejo", "delfin", "elefante", "gacela",
                "gorila", "jaguar", "jirafa", "koala", "leopardo", "mariposa", "nutria", "oveja",
                "pinguino", "tortuga", "tucan", "zorro"
            ),
            "Nombre de un animal del mundo natural."
        )
        addNounSet(
            listOf(
                "arbol", "bosque", "bruma", "cascada", "colina", "coral", "desierto", "estrella",
                "flor", "granizo", "isla", "laguna", "lluvia", "montana", "nube", "oceano",
                "piedra", "planeta", "rio", "volcan"
            ),
            "Palabra relacionada con la naturaleza y el paisaje."
        )
        addNounSet(
            listOf(
                "almohada", "armario", "banera", "cocina", "cortina", "escalera", "espejo", "estufa",
                "jarron", "lampara", "manta", "mesa", "puerta", "sabana", "sillon", "tapete",
                "vajilla", "ventana", "reloj", "cepillo"
            ),
            "Objeto que se puede encontrar en casa."
        )
        addNounSet(
            listOf(
                "agenda", "borrador", "cartilla", "cuaderno", "cuento", "dibujo", "estuche", "goma",
                "lapiz", "libro", "mapa", "mochila", "pizarron", "plastilina", "plumon", "regla",
                "tarea", "tijera", "tiza", "carpeta"
            ),
            "Palabra del mundo escolar y del aprendizaje."
        )
        addNounSet(
            listOf(
                "arepa", "arroz", "avena", "banana", "bizcocho", "brownie", "cacao", "cereal",
                "galleta", "helado", "limonada", "manzana", "merienda", "naranja", "paleta", "pan",
                "queso", "sopa", "torta", "yogur"
            ),
            "Nombre de una comida o bebida."
        )
        addNounSet(
            listOf(
                "avion", "barca", "bicicleta", "camion", "canoa", "carreta", "cohete", "ferry",
                "globo", "helicoptero", "lancha", "metro", "patin", "patineta", "remolque", "taxi",
                "tranvia", "tren", "trineo", "velero"
            ),
            "Palabra relacionada con el transporte y el movimiento."
        )
        addNounSet(
            listOf(
                "abrazo", "aliento", "cabeza", "cabello", "codo", "cuello", "dedo", "energia",
                "espalda", "estomago", "fuerza", "hombro", "hueso", "mano", "mejilla", "mirada",
                "nariz", "rodilla", "sonrisa", "tobillo"
            ),
            "Palabra relacionada con el cuerpo o el bienestar."
        )
        addNounSet(
            listOf(
                "aventura", "castillo", "disfraz", "dragon", "espada", "hada", "heroe", "juguete",
                "linterna", "magia", "mapa", "mascara", "misterio", "musica", "payaso", "pelota",
                "pirata", "robot", "tesoro", "trompo"
            ),
            "Palabra de juego, fantasia o diversion."
        )
        addNounSet(
            listOf(
                "acuarela", "baile", "cancion", "clarinete", "color", "dibujo", "flauta", "guitarra",
                "maraca", "melodia", "mural", "nota", "orquesta", "pintura", "piano", "poema",
                "ritmo", "tambor", "teatro", "violin"
            ),
            "Palabra relacionada con el arte, la musica o la expresion."
        )
        addNounSet(
            listOf(
                "aldea", "biblioteca", "camino", "campamento", "ciudad", "escuela", "granja", "jardin",
                "mercado", "museo", "parque", "plaza", "puerto", "refugio", "sendero", "templo",
                "torre", "tunel", "zoologico", "puente"
            ),
            "Nombre de un lugar que se puede visitar o imaginar."
        )

        addAdjectiveSet(
            listOf(
                "activo" to "activa", "alegre" to "alegre", "amistoso" to "amistosa",
                "atento" to "atenta", "aventurero" to "aventurera", "brillante" to "brillante",
                "calmado" to "calmada", "carinoso" to "carinosa", "creativo" to "creativa",
                "curioso" to "curiosa"
            ),
            "Describe una cualidad positiva o interesante."
        )
        addAdjectiveSet(
            listOf(
                "decidido" to "decidida", "divertido" to "divertida", "energico" to "energica",
                "famoso" to "famosa", "fantastico" to "fantastica", "firme" to "firme",
                "generoso" to "generosa", "gigante" to "gigante", "gracioso" to "graciosa",
                "habilidoso" to "habilidosa"
            ),
            "Sirve para contar como es alguien o algo."
        )
        addAdjectiveSet(
            listOf(
                "heroico" to "heroica", "ingenioso" to "ingeniosa", "jugueton" to "juguetona",
                "luminoso" to "luminosa", "magico" to "magica", "nervioso" to "nerviosa",
                "ordenado" to "ordenada", "pacifico" to "pacifica", "pegajoso" to "pegajosa",
                "poderoso" to "poderosa"
            ),
            "Describe caracteristicas, estados o apariencias."
        )
        addAdjectiveSet(
            listOf(
                "rapido" to "rapida", "risueno" to "risuena", "ruidoso" to "ruidosa",
                "sabroso" to "sabrosa", "salado" to "salada", "saltarin" to "saltarina",
                "seguro" to "segura", "silencioso" to "silenciosa", "suave" to "suave",
                "tenaz" to "tenaz"
            ),
            "Palabra para describir cualidades o sensaciones."
        )
        addAdjectiveSet(
            listOf(
                "tranquilo" to "tranquila", "valiente" to "valiente", "veloz" to "veloz",
                "amable" to "amable", "radiante" to "radiante", "curvilineo" to "curvilinea",
                "colorido" to "colorida", "fresco" to "fresca", "limpio" to "limpia",
                "pequeno" to "pequena"
            ),
            "Ayuda a imaginar mejor a personas, objetos o lugares."
        )
        addAdjectiveSet(
            listOf(
                "alto" to "alta", "bajo" to "baja", "dulce" to "dulce", "duro" to "dura",
                "feliz" to "feliz", "fino" to "fina", "grande" to "grande", "lento" to "lenta",
                "ligero" to "ligera", "listo" to "lista"
            ),
            "Palabra que muestra forma, tamano o manera de ser."
        )

        addVerbSet(
            listOf(
                "abrazar", "acampar", "acomodar", "adivinar", "adornar", "agitar", "alcanzar",
                "alimentar", "aplaudir", "apuntar", "armar", "atrapar", "ayudar", "bailar",
                "barrer", "beber", "bordar", "brillar", "buscar", "caminar"
            ),
            "Accion que se puede realizar jugando o aprendiendo."
        )
        addVerbSet(
            listOf(
                "cantar", "celebrar", "cepillar", "charlar", "clasificar", "cocinar", "colorear",
                "compartir", "comprender", "conectar", "contar", "correr", "cuidar", "decorar",
                "dejar", "descansar", "desarmar", "dibujar", "dividir", "doblar"
            ),
            "Accion que ayuda a mover, crear o descubrir."
        )
        addVerbSet(
            listOf(
                "educar", "elevar", "empujar", "encender", "encontrar", "ensayar", "ensenar",
                "escuchar", "esperar", "explorar", "fabricar", "felicitar", "firmar", "flotar",
                "formar", "ganar", "girar", "guardar", "habitar", "hablar"
            ),
            "Verbo para acciones de aprendizaje y movimiento."
        )
        addVerbSet(
            listOf(
                "imaginar", "imitar", "impulsar", "inventar", "investigar", "jugar", "juntar",
                "lavar", "mezclar", "levantar", "limpiar", "llegar", "llenar", "mirar", "modelar",
                "mover", "nadar", "narrar", "observar", "ordenar"
            ),
            "Accion frecuente en juegos, cuentos o tareas."
        )
        addVerbSet(
            listOf(
                "organizar", "pasear", "patear", "pegar", "pensar", "pintar", "planear", "plantar",
                "preguntar", "preparar", "probar", "proteger", "rebotar", "recibir", "recoger",
                "recordar", "recortar", "regalar", "regresar", "regar"
            ),
            "Verbo para hacer, crear, preguntar o avanzar."
        )
        addVerbSet(
            listOf(
                "rellenar", "remar", "repartir", "resbalar", "respetar", "respirar", "restar",
                "rodar", "rotar", "saltar", "saludar", "senalar", "sembrar", "separar", "susurrar",
                "sonar", "soplar", "subir", "sumar", "tejer"
            ),
            "Accion que aparece en juegos, cuentas o aventuras."
        )
        addVerbSet(
            listOf(
                "temer", "terminar", "tocar", "trabajar", "transformar", "transportar", "unir",
                "usar", "valorar", "viajar", "vigilar", "visitar", "volar", "vacilar", "zurcir",
                "aprender", "asistir", "bromear", "cosechar", "deslizar"
            ),
            "Verbo para descubrir, jugar y seguir explorando."
        )
    }.distinctBy { it.boardText }

    private fun MutableList<VocabularyWord>.addCoreWords() {
        val coreWords = listOf(
            VocabularyWord("Brisa", "Viento suave y agradable que refresca el ambiente."),
            VocabularyWord("Temple", "Serenidad y fortaleza para afrontar una dificultad."),
            VocabularyWord("Fulgor", "Resplandor intenso o brillo muy vivo."),
            VocabularyWord("Asombro", "Sorpresa grande causada por algo inesperado."),
            VocabularyWord("Lucidez", "Claridad mental para comprender o razonar."),
            VocabularyWord("Alivio", "Sensacion de mejora o disminucion de una carga."),
            VocabularyWord("Memoria", "Capacidad de recordar y conservar informacion."),
            VocabularyWord("Armonia", "Relacion equilibrada entre elementos distintos."),
            VocabularyWord("Anhelo", "Deseo intenso de conseguir algo."),
            VocabularyWord("Savia", "Liquido nutritivo que circula por las plantas."),
            VocabularyWord("Virtud", "Cualidad positiva que destaca en una persona."),
            VocabularyWord("Aventura", "Experiencia nueva, emocionante y divertida."),
            VocabularyWord("Burbuja", "Bola fina de aire rodeada por liquido."),
            VocabularyWord("Caricia", "Toque suave que expresa afecto o cuidado."),
            VocabularyWord("Fantasia", "Imaginacion que crea cosas maravillosas."),
            VocabularyWord("Fortuna", "Suerte favorable o buen resultado."),
            VocabularyWord("Gratitud", "Aprecio por algo bueno que se recibe."),
            VocabularyWord("Ilusion", "Esperanza alegre o entusiasmo por algo."),
            VocabularyWord("Pocion", "Mezcla imaginaria usada en cuentos y juegos."),
            VocabularyWord("Zafiro", "Piedra preciosa de color azul intenso.")
        )
        addAll(coreWords)
    }

    private fun MutableList<VocabularyWord>.addNounSet(
        words: List<String>,
        definition: String
    ) {
        words.forEach { word ->
            val singular = displayWord(word)
            val plural = displayWord(pluralize(word))
            add(VocabularyWord(singular, definition))
            add(VocabularyWord(plural, "Plural de $singular. $definition"))
        }
    }

    private fun MutableList<VocabularyWord>.addAdjectiveSet(
        words: List<Pair<String, String>>,
        definition: String
    ) {
        words.forEach { (masculineRaw, feminineRaw) ->
            val masculine = displayWord(masculineRaw)
            val feminine = displayWord(feminineRaw)
            val masculinePlural = displayWord(pluralize(masculineRaw))
            val femininePlural = displayWord(pluralize(feminineRaw))

            add(VocabularyWord(masculine, definition))
            add(VocabularyWord(feminine, "Forma femenina de $masculine. $definition"))
            add(VocabularyWord(masculinePlural, "Forma plural de $masculine. $definition"))
            add(VocabularyWord(femininePlural, "Forma plural de $feminine. $definition"))
        }
    }

    private fun MutableList<VocabularyWord>.addVerbSet(
        verbs: List<String>,
        definition: String
    ) {
        verbs.forEach { verb ->
            val infinitive = displayWord(verb)
            val gerund = displayWord(toGerund(verb))
            val participle = displayWord(toParticiple(verb))

            add(VocabularyWord(infinitive, definition))
            add(VocabularyWord(gerund, "Forma de accion continua del verbo $infinitive. $definition"))
            add(VocabularyWord(participle, "Forma del verbo $infinitive usada para describir una accion terminada. $definition"))
        }
    }

    private fun pluralize(word: String): String = when {
        word.endsWith("z") -> word.dropLast(1) + "ces"
        word.endsWith("s") || word.endsWith("x") -> word
        word.last() in listOf('a', 'e', 'i', 'o', 'u') -> word + "s"
        else -> word + "es"
    }

    private fun toGerund(verb: String): String = when (verb) {
        "leer" -> "leyendo"
        "reir" -> "riendo"
        "sonreir" -> "sonriendo"
        "ensenar" -> "ensenando"
        "senalar" -> "senalando"
        else -> if (verb.endsWith("ar")) verb.dropLast(2) + "ando" else verb.dropLast(2) + "iendo"
    }

    private fun toParticiple(verb: String): String = when (verb) {
        "leer" -> "leido"
        "reir" -> "reido"
        "sonreir" -> "sonreido"
        "ensenar" -> "ensenado"
        "senalar" -> "senalado"
        else -> if (verb.endsWith("ar")) verb.dropLast(2) + "ado" else verb.dropLast(2) + "ido"
    }

    private fun displayWord(word: String): String =
        wordDisplayOverrides[word]
            ?: word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

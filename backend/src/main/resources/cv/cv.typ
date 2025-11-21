#set page(
    margin: (left: 20mm, right: 20mm, top: 20mm, bottom: 10mm),
    numbering: none,
    number-align: center,
  )

#set text(
  font: "Calibri",
  size: 10pt,
)

#set par(justify: false, leading: 0.65em)

// Helper function for skill bars
#let skill-bar(level) = {
  let total-width = 100%
  let filled = level
  let empty = 100 - level

  box(
    width: total-width,
    height: 8pt,
    baseline: 20%,
    stack(
      dir: ltr,
      rect(
        width: (filled * 1%),
        height: 100%,
        fill: rgb("#666666"),
      ),
      rect(
        width: (empty * 1%),
        height: 100%,
        fill: rgb("#cccccc"),
      ),
    )
  )
}

#align(right + top)[
#text(size: 13pt)[*Jan van de Locht*]\
Mobil 0151 65 600 448 | jan\@vandelocht.uk \
Teutstraße 33 | 46117 Oberhausen #h(0.15em) \
github.com/JvandeLocht\
codeberg.org/JvandeLocht
]
#align(left + top)[
  #line(
    length: 100% + 30mm,
    stroke: 4pt + rgb("#DDE1E6"),
  )
]

// Main layout with two columns
#grid(
  columns: (75%, 25%),
  gutter: 1.5em,
  [
    // Left column - Main content
    #v(2em)

    // Beruflicher Werdegang
    #text(size: 13pt, weight: "bold")[Beruflicher Werdegang]
    #v(0.5em)

    #grid(
      columns: (28%, 72%),
      gutter: 1em,
      row-gutter: 1em,

      [Seit 08/2023],
      [*Arbeitsvorbereitung*\
      MAN Energy Solutions SE, Oberhausen],

      [Seit 01/2021],
      [*Vorrichtungskonstrukteur*\
      MAN Energy Solutions SE, Oberhausen],

      [02/2013 – 12/2020],
      [*Industriemechaniker Fachrichtung*\
      *Maschinen- und Anlagenbau*\
      MAN Energy Solutions SE, Oberhausen],
    )

    // Ausbildung
    #text(size: 13pt, weight: "bold")[Ausbildung]
    #v(0.5em)

    #grid(
      columns: (28%, 72%),
      gutter: 1em,
      row-gutter: 1em,
      [09/2009 – 02/2013],
      [*Ausbildung zum Industriemechaniker in der Fachrichtung*\
      *Maschinen und Anlagenbau*\
      MAN Energy Solutions SE, Oberhausen],
    )

    // Weiterbildung
    #text(size: 13pt, weight: "bold")[Weiterbildung]
    #v(0.5em)

    #grid(
      columns: (28%, 72%),
      gutter: 1em,
      row-gutter: 1em,

      [01/2020 - 10/2024],
      [*Studium zum Bachelor of Engineering*\
      Schwerpunkt: Smart Products & Services\
      Hamburger Fern-Hochschule],

      [2023],
      [*Gastvorlesung Harvard*\
      CS50\
      Artificial Intelligence with Python],

      [2022],
      [*Gastvorlesung Universität Wien*\
      Einführung in die Vektor- und Tensorrechnung 1\
      Einführung in die Physik 1\
      Einführung in die Physik 2],

      [08/2015 – 06/2019],
      [*Staatlich geprüfter Techniker in der Fachrichtung Maschinenbautechnik*\
      Technische Berufliche Schule 1, Bochum],
    )
  ],
  [
    #align(right)[
      // Profile picture placeholder
      // TODO: Add pic.jpg to enable profile picture
      // #image("pic.jpg", width: 100%)
    ]
  ]
)

// Kenntnisse
#text(size: 13pt, weight: "bold")[Kenntnisse]
#v(0.5em)
 #grid(
  columns: (50%, 50%),
  gutter: 2em,
  [
    #text(weight: "bold")[EDV-Kenntnisse]
    #v(0.3em)
    #grid(
      columns: (40%, 60%),
      gutter: 0.5em,
      row-gutter: 0.4em,

      align(left)[Siemens NX], skill-bar(85),
      align(left)[ECTR], skill-bar(60),
      align(left)[SAP], skill-bar(40),
      align(left)[ANSYS], skill-bar(35),
      align(left)[GIT], skill-bar(35),
    )
  ],
  [
    #text(weight: "bold")[Programmiersprachen]
    #v(0.3em)
    #grid(
      columns: (40%, 60%),
      gutter: 0.5em,
      row-gutter: 0.4em,

      align(left)[Python], skill-bar(60),
      align(left)[Nix], skill-bar(45),
      align(left)[Bash], skill-bar(40),
      align(left)[C], skill-bar(10),
    )
  ],
  [
    #text(weight: "bold")[Sprachkenntnisse]
    #v(0.3em)
    #grid(
      columns: (40%, 60%),
      gutter: 0.5em,
      row-gutter: 0.4em,

      align(left)[Deutsch], skill-bar(95),
      align(left)[Englisch], skill-bar(70),
    )
  ],
  [
    // Interessen
    #text(weight: "bold")[Interessen]
    #v(0.3em)
    Sport\
    Systemadministration (Kubernetes/ Linux/ Networking)
  ]
)


#align(left + bottom)[
  #line(
    length: 100% + 30mm,
    stroke: 4pt + rgb("#DDE1E6"),
  )
]

#set align(right + bottom)
Oberhausen, 21.11.2024

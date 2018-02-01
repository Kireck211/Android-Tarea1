package com.example.erick.tarea01.dto

class Student {
        private var name: String
        private var phone: String
        private var studies: String
        private var gender: String
        private var favorite: String?
        private var sports: Boolean

        constructor(name: String, phone: String, studies: String, gender: String, sports: Boolean, favorite: String?) {
                this.name = name
                this.phone = phone
                this.studies = studies
                this.gender = gender
                this.favorite = favorite
                this.sports = sports
        }

        override fun toString(): String {
                return "Nombre: ${this.name}\nTelefono: ${this.phone}\nEscolaridad: ${this.studies}\nGenero: ${this.gender}\n${if (this.favorite != null) "Libro Favorito: " + this.favorite + "\n" else ""}Practica Deporte: ${if (this.sports) "Si" else "No"}"
        }
}
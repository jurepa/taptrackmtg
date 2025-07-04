# TaptrackMTG

TaptrackMTG es una aplicación Android inspirada en [LifeTap](https://play.google.com/store/apps/details?id=com.lifetap&hl=es_419&pli=1) y Manabox, diseñada para jugadores de Magic: The Gathering. Permite principalmente dos cosas:
- Realizar búsquedas rápidas de cartas, guardar tus favoritas y realizar un seguimiento automático de los precios de mercado.
- Monitorizar las vidas de las partidas, adaptable para todos los tipos de modos de juego y además con ayuda de ruling si surgen dudas sobre mecánicas durante la partida.

## ✨ Funcionalidades principales

- 🔍 **Búsqueda de cartas:** Consulta detalles de cualquier carta del juego mediante una búsqueda rápida por nombre.
- ❤️ **Cartas favoritas:** Marca tus cartas favoritas para tenerlas siempre a mano.
- 📈 **Seguimiento de precios:** La app comprueba periódicamente el precio de tus cartas marcadas y **te envía una notificación si el precio sube o baja drásticamente (por ejemplo, ±5 €)**.
- ☁️ **Datos actualizados:** Usa la API de [Scryfall](https://scryfall.com/docs/api) para obtener datos actualizados de cartas y precios.

## 📱 Tecnologías usadas

- Java + Android SDK
- Retrofit2 para llamadas a la API
- Room para almacenamiento local
- LiveData y ViewModel (arquitectura MVVM)
- Procesos en segundo plano con Worker
- Notificaciones push locales

## 🚧 Funcionalidades en desarrollo

- Soporte para múltiples listas personalizadas
- Seguimiento histórico de precios
- Alerta configurable por cantidad de precio que sube/baja y por la frecuencia con la que se requiere que se compruebe el precio


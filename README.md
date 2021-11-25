# JHipsterExercise

This application was generated using JHipster 7.3.1, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v7.3.1](https://www.jhipster.tech/documentation-archive/v7.3.1).

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

We use npm scripts and [Angular CLI][] with [Webpack][] as our build system.

membres : Quentin Dethy, Yowan Kerdjou, Lucile Comba-Antonetti, Nicolas Heissler

tâches : Intégrations d'API : Paiement Paypal, Adyen (paiement CB), Google Sign-up
Intégrations de fausse pubs
Sécurité avec eMails
Monitoring (connexions, requêtes)

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

membres : Matthieu GONIN, Thibaut ABELANEDA, Nathan DESPRES

tâches : Front End

JHipster ships with PWA (Progressive Web App) support, and it's turned off by default. One of the main components of a PWA is a service worker.

membres : Guillaume VALETTE, Teddy JACONO, Abdelkhalek EL OMARI, Abdelhamid BASSA

tâches : BDD -> Recherche,filtrage,données,monitoring access

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

```
npm install --save --save-exact leaflet
```

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

```
npm install --save-dev --save-exact @types/leaflet
```

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Edit [src/main/webapp/app/app.module.ts](src/main/webapp/app/app.module.ts) file:

```
import 'leaflet/dist/leaflet.js';
```

Edit [src/main/webapp/content/scss/vendor.scss](src/main/webapp/content/scss/vendor.scss) file:

```
@import '~leaflet/dist/leaflet.css';
```

Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.module.ts
```

### JHipster Control Center

Lien du repository du groupe 1 : https://github.com/Nico-Heissler/JHipsterExercise/

Lien du repository du groupe 2 : https://github.com/Assleiv/JHipsterExercise

Lien du repository du groupe 3 : https://github.com/guillval/JHipsterExercise

### Packaging as jar

To build the final jar and optimize the JHipsterExercise application for production, run:

```
./mvnw -Pprod clean verify
```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

Une fois la fonctionnalité développée et fonctionnelle, le groupe devra effectuer une pull request sur le repository principal afin que la fonctionnalité puisse être intégrée au projet.

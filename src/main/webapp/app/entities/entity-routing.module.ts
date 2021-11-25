import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'client',
        data: { pageTitle: 'jHipsterExerciseApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'commande',
        data: { pageTitle: 'jHipsterExerciseApp.commande.home.title' },
        loadChildren: () => import('./commande/commande.module').then(m => m.CommandeModule),
      },
      {
        path: 'connexion',
        data: { pageTitle: 'jHipsterExerciseApp.connexion.home.title' },
        loadChildren: () => import('./connexion/connexion.module').then(m => m.ConnexionModule),
      },
      {
        path: 'produit',
        data: { pageTitle: 'jHipsterExerciseApp.produit.home.title' },
        loadChildren: () => import('./produit/produit.module').then(m => m.ProduitModule),
      },
      {
        path: 'caracteristique',
        data: { pageTitle: 'jHipsterExerciseApp.caracteristique.home.title' },
        loadChildren: () => import('./caracteristique/caracteristique.module').then(m => m.CaracteristiqueModule),
      },
      {
        path: 'famille',
        data: { pageTitle: 'jHipsterExerciseApp.famille.home.title' },
        loadChildren: () => import('./famille/famille.module').then(m => m.FamilleModule),
      },
      {
        path: 'sous-famille',
        data: { pageTitle: 'jHipsterExerciseApp.sousFamille.home.title' },
        loadChildren: () => import('./sous-famille/sous-famille.module').then(m => m.SousFamilleModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

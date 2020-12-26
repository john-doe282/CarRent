import React from 'react';
import { Switch } from 'react-router-dom';
import AvailableCar from './available-car';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Location from './location';
import Model from './model';
import Type from './type';
import Car from './car';
import ActiveRent from './active-rent';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}location`} component={Location} />
      <ErrorBoundaryRoute path={`${match.url}model`} component={Model} />
      <ErrorBoundaryRoute path={`${match.url}type`} component={Type} />
      <ErrorBoundaryRoute path={`${match.url}car`} component={Car} />
      <ErrorBoundaryRoute path={`${match.url}available-car`} component={AvailableCar} />
      <ErrorBoundaryRoute path={`${match.url}active-rent`} component={ActiveRent} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;

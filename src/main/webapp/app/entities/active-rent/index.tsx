import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ActiveRent from './active-rent';
import ActiveRentDetail from './active-rent-detail';
import ActiveRentUpdate from './active-rent-update';
import ActiveRentDeleteDialog from './active-rent-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ActiveRentUpdate} />
      {/*<ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ActiveRentUpdate} />*/}
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ActiveRentDetail} />
      <ErrorBoundaryRoute path={match.url} component={ActiveRent} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ActiveRentDeleteDialog} />
  </>
);

export default Routes;

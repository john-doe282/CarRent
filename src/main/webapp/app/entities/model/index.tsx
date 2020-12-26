import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Model from './model';
import ModelDetail from './model-detail';
import ModelUpdate from './model-update';
import ModelDeleteDialog from './model-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ModelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ModelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ModelDetail} />
      <ErrorBoundaryRoute path={match.url} component={Model} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ModelDeleteDialog} />
  </>
);

export default Routes;

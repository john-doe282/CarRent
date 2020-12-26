import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IModel } from 'app/shared/model/model.model';
import { getEntities as getModels } from 'app/entities/model/model.reducer';
import { IType } from 'app/shared/model/type.model';
import { getEntities as getTypes } from 'app/entities/type/type.reducer';
import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './car.reducer';
import { ICar } from 'app/shared/model/car.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICarUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CarUpdate = (props: ICarUpdateProps) => {
  const [modelId, setModelId] = useState('0');
  const [typeId, setTypeId] = useState('0');
  const [locationId, setLocationId] = useState('0');
  const [ownerId, setOwnerId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { carEntity, models, types, locations, users, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/car' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getModels();
    props.getTypes();
    props.getLocations();
    props.getUsers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...carEntity,
        ...values,
      };
      entity.owner = users[0]
      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rentalApp.car.home.createOrEditLabel">Create or edit a Car</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : carEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="car-id">ID</Label>
                  <AvInput id="car-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="descriptionLabel" for="car-description">
                  Description
                </Label>
                <AvField
                  id="car-description"
                  type="text"
                  name="description"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="car-status">
                  Status
                </Label>
                <AvInput
                  id="car-status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && carEntity.status) || 'AVAILABLE'}
                >
                  <option value="AVAILABLE">AVAILABLE</option>
                  <option value="RENTED">RENTED</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="pricePerHourLabel" for="car-pricePerHour">
                  Price Per Hour
                </Label>
                <AvField
                  id="car-pricePerHour"
                  type="text"
                  name="pricePerHour"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    min: { value: 0, errorMessage: 'This field should be at least 0.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="car-model">Model</Label>
                <AvInput
                  id="car-model"
                  type="select"
                  className="form-control"
                  name="model.id"
                  value={isNew ? models[0] && models[0].id : carEntity.model?.id}
                  required
                >
                  {models
                    ? models.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>This field is required.</AvFeedback>
              </AvGroup>
              <AvGroup>
                <Label for="car-type">Type</Label>
                <AvInput
                  id="car-type"
                  type="select"
                  className="form-control"
                  name="type.id"
                  value={isNew ? types[0] && types[0].id : carEntity.type?.id}
                  required
                >
                  {types
                    ? types.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>This field is required.</AvFeedback>
              </AvGroup>
              <AvGroup>
                <Label for="car-location">Location</Label>
                <AvInput
                  id="car-location"
                  type="select"
                  className="form-control"
                  name="location.id"
                  value={isNew ? locations[0] && locations[0].id : carEntity.location?.id}
                  required
                >
                  {locations
                    ? locations.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.district}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>This field is required.</AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/car" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  models: storeState.model.entities,
  types: storeState.type.entities,
  locations: storeState.location.entities,
  users: storeState.userManagement.users,
  carEntity: storeState.car.entity,
  loading: storeState.car.loading,
  updating: storeState.car.updating,
  updateSuccess: storeState.car.updateSuccess,
});

const mapDispatchToProps = {
  getModels,
  getTypes,
  getLocations,
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CarUpdate);

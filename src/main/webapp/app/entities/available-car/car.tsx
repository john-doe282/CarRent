import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Select from 'react-select'

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './car.reducer';
import {getEntities as getModels} from 'app/entities/model/model.reducer';
import {getEntities as getLocations} from 'app/entities/location/location.reducer';
import { ICar } from 'app/shared/model/car.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface ICarProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Car = (props: ICarProps) => {
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE), props.location.search)
  );

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
    props.getLocations()
    props.getModels()
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get('sort');
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const { carList, match, loading, totalItems, locations } = props;

  const [selectedLocation, setSelectedLocations] = useState({ value: 0, label: 'all' });
  let options = [{value: 0, label: 'all'}]
  options = options.concat(locations.map(location => {
    return {value: location.id, label: location.district}
  }))
  return (
    <div>
      <h2 id="car-heading">
        Available Cars
      </h2>
      <Select
        onChange={value => {
          setSelectedLocations(value);
        }}
        options={options}
      />
      <div className="table-responsive">
        {carList && carList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('description')}>
                  Description <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  Status <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('pricePerHour')}>
                  Price Per Hour <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Model <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Type <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Location <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Owner <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {carList.
              filter(car => selectedLocation.label === 'all' || car.location.id === selectedLocation.value).
              map((car, i) => (
                <tr key={`entity-${i}`}>
                  <td>{car.description}</td>
                  <td>{car.status}</td>
                  <td>{car.pricePerHour}</td>
                  <td>{car.model ? <Link to={`model/${car.model.id}`}>{car.model.name}</Link> : ''}</td>
                  <td>{car.type ? <Link to={`type/${car.type.id}`}>{car.type.name}</Link> : ''}</td>
                  <td>{car.location ? <Link to={`location/${car.location.id}`}>{car.location.district}</Link> : ''}</td>
                  <td>{car.owner ? car.owner.login : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${car.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${car.id}/rent?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Rent</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Cars found</div>
        )}
      </div>
      {props.totalItems ? (
        <div className={carList && carList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </Row>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={props.totalItems}
            />
          </Row>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

const mapStateToProps = (state: IRootState) => ({

  carList: state.car.entities,
  loading: state.car.loading,
  totalItems: state.car.totalItems,
  locations: state.location.entities,
  models: state.model.entities,
});

const mapDispatchToProps = {
  getEntities,
  getLocations,
  getModels,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Car);

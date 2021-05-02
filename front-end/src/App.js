import React, {Suspense, useState} from 'react';
import {Redirect, Route, Switch} from 'react-router-dom';

import Layout from './components/layout/Layout';
import Spinner from './components/commons/Spinner';
import Pricelist from './components/pricelist/Pricelist';
import ProductList from './components/product/ProductList';

const Calculator = React.lazy(() => import('./components/calculator/Calculator'));

const App = props => {
    const [productId, setProductId] = useState(null);

    return (
        <Layout>
            <Suspense fallback={<Spinner/>}>
                <Switch>
                    <Route path="/" render={props => <ProductList onSelect={setProductId} {...props}/>}/>
                </Switch>
                <Switch>
                    <Route path="/calculator" render={props => <Calculator productId={productId} {...props}/>}/>
                    <Route path="/pricelist" render={props => <Pricelist productId={productId} {...props}/>}/>
                    <Redirect to="/pricelist"/>
                </Switch>
            </Suspense>
        </Layout>
    );
};

export default App;

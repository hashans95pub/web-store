import Product from './Product';
import classes from './ProductList.module.css';
import useHttp from "../../hooks/use-http";
import {getAllProducts} from "../../lib/api";
import {useEffect} from "react";
import Spinner from "../commons/Spinner";

const ProductList = (props) => {
    const { sendRequest, status, data: products, error } = useHttp(
        getAllProducts,
        true
    );

    useEffect(() => {
        sendRequest();
    }, [sendRequest]);

    if (status === 'pending') {
        return (
            <div className='centered'>
                <Spinner />
            </div>
        );
    }

    if (error) {
        return <p className='centered focused'>{error}</p>;
    }

    if (status === 'completed' && (!products || products.length === 0)) {
        return <p className='centered focused'>No Products Found</p>;
    }

    return (
        <div className={classes['product-list']}>
            {products.map((product) => (
                <Product
                    key={product.id}
                    id={product.id}
                    name={product.name}
                    onSelect={props.onSelect}
                />
            ))}
        </div>
    );
};

export default ProductList;
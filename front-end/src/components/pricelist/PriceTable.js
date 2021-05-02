import {useEffect} from "react";
import Table from 'react-bootstrap/Table'

import useHttp from "../../hooks/use-http";
import {getPriceList} from "../../lib/api";

const PriceTable = props => {
    const {sendRequest, data: pricelist} = useHttp(
        getPriceList,
        true
    );

    useEffect(() => {
        if (props.selProduct) {
            sendRequest({
                productId: props.selProduct,
                noOfUnits: 50
            });
        }
    }, [sendRequest, props.selProduct]);

    return (
        <div className="container">
            <h1>Price List</h1>
            <Table striped bordered hover size="sm">
                <thead>
                <tr>
                    <th>No of Items</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                {pricelist !== null ?
                    pricelist.map((item) => (
                        <tr key={item.noOfUnits}>
                            <td>{item.noOfUnits}</td>
                            <td className="text-right">{item.price.toFixed(2)}</td>
                        </tr>
                    )) : <tr>
                        <td colSpan="2">Please select a product</td>
                    </tr>
                }
                </tbody>
            </Table>
        </div>
    );
};

export default PriceTable;
import Card from 'react-bootstrap/Card'

import classes from './Product.module.css';

const product = props => {
    return (
        <Card className={classes.product} style={{width: '18rem'}} onClick={() => props.onSelect(props.id)}>
            <Card.Img variant="top" src={require(`../../assets/product-${props.id}.jpg`).default}/>
            <Card.Body>
                <Card.Title>{props.name}</Card.Title>
            </Card.Body>
        </Card>
    )
};

export default product;
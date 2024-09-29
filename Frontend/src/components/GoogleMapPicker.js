import React, { useState } from "react";
import GoogleMapReact from "google-map-react";

const Marker = ({ text }) => (
  <div
    style={{
      color: "white",
      padding: "0px 0px",
      borderRadius: "0",
      transform: "translate(-50%, -50%)",
      position: "absolute",
    }}
  >
    {text}
  </div>
);

const defaultProps = {
  center: {
    lat: 37.7749, // Default latitude (San Francisco)
    lng: -122.4194, // Default longitude (San Francisco)
  },
  zoom: 10,
};

export default function GoogleMapPicker({ setCoordinates }) {
  const [marker, setMarker] = useState(null);

  const handleClick = ({ lat, lng }) => {
    const position = { lat, lng };
    console.log("Marker position:", position); // Debugging information
    setMarker(position);
    setCoordinates(position);
  };

  return (
    <div style={{ height: "300px", width: "70%", position: "relative" }}>
      <GoogleMapReact
        bootstrapURLKeys={{ key: "Your API KEY" }} // Replace with your API key
        defaultCenter={defaultProps.center}
        defaultZoom={defaultProps.zoom}
        onClick={handleClick}
      >
        {marker && <Marker lat={marker.lat} lng={marker.lng} text="📍" />}
      </GoogleMapReact>
    </div>
  );
}

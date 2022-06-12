package com.travel.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.stereotype.Service;

import com.travel.dto.DecryptedVehicleDto;
import com.travel.dto.VehicleDto;
import com.travel.model.Vehicle;

@Service
public class VehicleMapper {

    private static SecretKey KEY;
    private static IvParameterSpec IV_PARAMETER_SPEC;
    private static String ALGORITHM;

    @PostConstruct
    void init(){
        try {
            KEY = generateKey(128);
            IV_PARAMETER_SPEC = generateIv();
            ALGORITHM = "AES/CBC/PKCS5Padding";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //Cyphers registration plates - decrypting is done in the @toDecryptedVehicleRegistrationDto method
    public Vehicle toModel(final VehicleDto dto){
        final Vehicle model = new Vehicle();

        model.setId(dto.getId());
        model.setModel(dto.getModel());

        try {
            final String input = dto.getRegistration();
            final String cipheredVehicleRegistration = encrypt(ALGORITHM, input, KEY, IV_PARAMETER_SPEC);
            System.out.println("Ciphered " + dto.getRegistration() + " successfully into " + cipheredVehicleRegistration);
            model.setRegistration(cipheredVehicleRegistration);
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }

        model.setImage(dto.getImage().getBytes(StandardCharsets.UTF_8));

        return model;
    }

    public VehicleDto toDto(final Vehicle model){
        final VehicleDto dto = new VehicleDto();

        dto.setId(model.getId());
        dto.setModel(model.getModel());
        dto.setRegistration(model.getRegistration());
        dto.setImage(Arrays.toString(model.getImage()));

        return dto;
    }

    public static String encrypt(final String algorithm, final String input, final SecretKey key, final IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        final Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        final byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(final String algorithm, final String cipherText, final SecretKey key, final IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        final Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        final byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }

    public static SecretKey generateKey(final int n) throws NoSuchAlgorithmException {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public DecryptedVehicleDto toDecryptedVehicleRegistrationDto(final Vehicle model) {
        final DecryptedVehicleDto decryptedVehicleDto = new DecryptedVehicleDto();

        decryptedVehicleDto.setId(model.getId());
        try {
            decryptedVehicleDto.setRegistration(decrypt(ALGORITHM, model.getRegistration(), KEY, IV_PARAMETER_SPEC));
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return decryptedVehicleDto;
    }

}
